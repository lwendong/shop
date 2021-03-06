package dao.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import po.Notice;

@Repository("adminNoticeDao")
@Mapper
public interface AdminNoticeDao {
	
	public int addNotice(Notice notice);
	
	public List<Notice> deleteNoticeSelect();
	
	public List<Notice> deleteNoticeSelectBypage(Map<String, Object> map);
	
	public int deleteNotice(String noticeId);
	
	public Notice selectNoticeOne(String noticeId);
}
