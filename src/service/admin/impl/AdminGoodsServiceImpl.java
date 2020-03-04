package service.admin.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import dao.admin.AdminGoodsDao;
import dao.admin.AdminTypeDao;
import po.Goods;
import po.GoodsType;
import service.admin.AdminGoodsService;
import util.MyUtil;
@Service("adminGoodsService")
@Transactional
public class AdminGoodsServiceImpl implements AdminGoodsService{
	@Autowired
	private AdminGoodsDao adminGoodsDao;
	@Autowired
	private AdminTypeDao adminTypeDao;
	/**
	 * ���ӻ����
	 */
	public String addOrUpdateGoods(Goods goods, HttpServletRequest request, String updateAct) {
		/*�ϴ��ļ��ı���λ��"/logos"����λ����ָ
		workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps��
		������ʹ��*/
		//��ֹ�ļ�������
		String newFileName = "";
		String fileName = goods.getLogoImage().getOriginalFilename(); 
		//ѡ�����ļ�
		if(fileName.length() > 0){
			String realpath = request.getServletContext().getRealPath("img");
			//ʵ���ļ��ϴ�
			String fileType = fileName.substring(fileName.lastIndexOf('.'));
			//��ֹ�ļ�������
			newFileName = MyUtil.getStringID() + fileType;
			goods.setImg(newFileName);
			File targetFile = new File(realpath, newFileName); 
			if(!targetFile.exists()){  
	            targetFile.mkdirs();  
	        } 
			//�ϴ� 
	        try {   
	        	goods.getLogoImage().transferTo(targetFile);
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		}
		//�޸�
		if("update".equals(updateAct)){//updateAct������act��������Ϊʹ����ת��
			//�޸ĵ����ݿ�
	       if(adminGoodsDao.updateGoodsById(goods) > 0){
	        	return "forward:/adminGoods/selectGoods?act=updateSelect";
	        }else{
	        	return "/adminGoods/updateAgoods";
	       }
		}else{
			goods.setId(MyUtil.getUUID());
			//���浽���ݿ�
			if(adminGoodsDao.addGoods(goods) > 0){
				//ת������ѯ��controller
				return "forward:/adminGoods/selectGoods";
			}else{
				return "card/addCard";
			}
		}
	}
	/**
	 * ��ѯ��Ʒ
	 */
	public String selectGoods(Model model, Integer pageNo, String delAndUp) {
		List<Goods> allGoods = adminGoodsDao.selectGoods();
		int temp = allGoods.size();
		model.addAttribute("totalCount", temp);
		int totalPage = 0;
		if (temp == 0) {
			totalPage = 0;//��ҳ��
		} else {
			//���ش��ڻ��ߵ���ָ������ʽ����С����
			totalPage = (int) Math.ceil((double) temp / 10);
		}
		if (pageNo == null) {
			pageNo = 1;
		}
		//��ҳ��ѯ
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", (pageNo - 1) * 10);//��ʼλ��
		map.put("pageSize", 10);//ÿҳ10��
		
		allGoods = adminGoodsDao.selectAllGoodsByPage(map);
		model.addAttribute("allGoods", allGoods);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("pageNo", pageNo);
		return "admin/selectGoods";
	}
	/**
	 * ��ѯһ����Ʒ
	 */
	public String selectGoodsOne(Model model, String goodsId, String act) {
		Goods goods = adminGoodsDao.selectGoodsById(goodsId);
		model.addAttribute("goods", goods);
		//����ҳ��
		if("act".equals(act)) {
			List<GoodsType> goodsType = adminTypeDao.selectGoodsType();
			model.addAttribute("goodsType",goodsType);
			return "admin/updateAgoods";
		}else {
			return "admin/goodsDetail";
		}
	}
	/**
	 * ɾ�������Ʒ
	 */
	public Map<String, Object> deleteGoods(String[] goodsIds, Model model) {
		Map<String, Object> map=new HashMap<String,Object>();
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		for (int i = 0; i < goodsIds.length; i++) {
			//��Ʒ�й���
			if(adminGoodsDao.selectCartGoods(goodsIds[i]).size() > 0 ||
				adminGoodsDao.selectFocusGoods(goodsIds[i]).size() > 0 ) {
				list2.add(goodsIds[i]);
			}else {
				list.add(goodsIds[i]);
			}
		}
		
		if(list2.size()!= 0) {
			map.put("message", list2);
		}else {
			if(list.size()!= 0) {
				adminGoodsDao.deleteGoods(list);
				map.put("message", 0);
			}
		}
		
		return map;
	}
	/**
	 * ɾ��һ����Ʒ
	 */
	public Map<String, Object> deleteGoodsOne(String goodsId, Model model) {
		Map<String, Object> map=new HashMap<String,Object>();
		//��Ʒ�й���
		if(adminGoodsDao.selectCartGoods(goodsId).size() > 0 ||
			adminGoodsDao.selectFocusGoods(goodsId).size() > 0) {
			map.put("message", "��Ʒ�й�����������ɾ����");
		}else {
			adminGoodsDao.deleteGoodsOne(goodsId);
			map.put("message", "ɾ���ɹ�");
		}
		return map;
	}

	
}