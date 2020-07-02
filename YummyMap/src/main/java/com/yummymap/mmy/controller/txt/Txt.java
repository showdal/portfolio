package com.yummymap.mmy.controller.txt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.yummymap.mmy.Service.txt.TxtService;
import com.yummymap.mmy.dao.TxtDAO;
import com.yummymap.mmy.util.PageUtil;
import com.yummymap.mmy.vo.txt.TxtVO;

@Controller
@RequestMapping("/txt")
public class Txt {
	@Autowired
	TxtDAO tDAO;
	
	@Autowired
	TxtService tService;

	@RequestMapping("/list.mmy")
	public ModelAndView list(ModelAndView mv, TxtVO tVO, PageUtil page) {
		String view = "txt/list";
		ArrayList<TxtVO> list = (ArrayList<TxtVO>) tDAO.getList();
		mv.addObject("LIST", list);
		mv.addObject("PAGE", page);
		mv.setViewName(view);
		return mv;
	}

	@RequestMapping(value = "/write.mmy")
	public String write() {
		String view = "txt/write";
		return view;
	}
	
	@RequestMapping(value ="/writeProc.mmy" ,method=RequestMethod.POST )
	public ModelAndView writeProc(ModelAndView mv, TxtVO tVO,HttpSession session ) {
		  int cnt = tDAO.write(tVO);
		  cnt = tService.file(tVO, session);
			 if(cnt==1) {
				 mv.setView(new RedirectView("/yummymap/txt/list.mmy"));
			 }
		return mv;
	}

	@RequestMapping(value="/detail.mmy")
	public ModelAndView detail(String mid, @RequestParam("txtno")int txtno, ModelAndView mv, TxtVO tVO, String isrec, HttpSession session) {
		tVO.setTxtno(txtno);
		String view = "/yummymap/txt/detail.mmy?txtno=" + tVO.getTxtno();
		TxtVO vo = tDAO.detail(txtno);
		tDAO.count(txtno);
		vo.setTxtno(txtno);
		tDAO.checkLike(tVO);
		tVO.setIsrec(isrec);
		mv.addObject("DATA", vo);
		return mv;
	}

	@RequestMapping(value = "/delete.mmy", method = RequestMethod.POST, params = {"txtno"})
	public ModelAndView delete(int txtno, TxtVO tVO, ModelAndView mv) {
		String view = "txt/list";
		tVO.setTxtno(txtno);
		int cnt = tDAO.delete(txtno);
			RedirectView rv = new RedirectView("/yummymap/txt/list.mmy");
			mv.setView(rv);
		return mv;
	}

	@RequestMapping(value = "/edit.mmy", method = RequestMethod.POST)
	public ModelAndView edit(TxtVO tVO, ModelAndView mv, HttpSession session) {
		String view = "txt/edit";
		TxtVO vo = tDAO.edit(tVO);
		mv.addObject("DATA", vo);
		return mv;
	}

	@RequestMapping(value = "/editProc.mmy" ,method = RequestMethod.POST)
	public ModelAndView editProc(TxtVO tVO, ModelAndView mv,HttpSession session) {
		String view = "txt/edit";
		
		tVO.setTxtno(tVO.getTxtno());
		tDAO.edit(tVO);
		System.out.println(tVO.getTxtno());
		
			mv.setView(new RedirectView("/yummymap/txt/detail.mmy?txtno=" + tVO.getTxtno()));
		return mv; 
	}
	
	@RequestMapping(value="/rList.mmy")
	@ResponseBody
	public List<TxtVO> rList(int txtno, String mtxt, TxtVO tVO, HttpServletRequest req) { 
		ArrayList<TxtVO> rList = (ArrayList<TxtVO>)tDAO.rList(txtno);
		tVO.setUpno(txtno);
		req.setAttribute("LIST", rList);
		return rList;
	 }
	
	@RequestMapping(value="/rWrite.mmy", method = RequestMethod.POST, params = {"mtxt","mid"})
	@ResponseBody
	public TxtVO rWrite(TxtVO tVO, ModelAndView mv, int upno, HttpSession session, String mid, String mtxt) {
		String veiw = "txt/detail";
		tVO.setUpno(upno);
		TxtVO vo= tDAO.rWrite(tVO);
		return vo;
	}
	
	@RequestMapping(value="/rDelete.mmy", method=RequestMethod.POST, params= {"txtno","upno"})
	@ResponseBody
	public TxtVO rDelete(int txtno, int upno, TxtVO tVO, HttpSession session) {
		tVO.setUpno(upno);
		tDAO.rDelete(tVO);
		return tVO;
	}
	
	@RequestMapping(value = "/like.mmy", method=RequestMethod.POST, params= {"txtno"})
	@ResponseBody
	public TxtVO like(int txtno, TxtVO tVO, HttpSession session, ModelAndView mv) {
		int cnt = tDAO.checkLike(tVO);
		mv.addObject("DATA",tVO);
		if(cnt == 0) {
			tDAO.addLike(tVO);
		}
		else {
			tDAO.editLike(tVO);
		}
		
		int rnum = tDAO.likeCnt(txtno);
		tVO.setRnum(rnum);
		return tVO;
	}

	@RequestMapping(value="/likeCnt.mmy", method=RequestMethod.POST, params= {"txtno","rnum"})
	@ResponseBody
	public int like(int rnum, int txtno, TxtVO tVO) {
		rnum = tDAO.likeCnt(txtno);
		tVO.setTxtno(txtno);
		tVO.setRnum(rnum);
		return rnum;
	}
}
