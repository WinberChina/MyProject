package com.mx.winber.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mx.winber.model.Course;
import com.mx.winber.service.CourseService;

@Controller
@RequestMapping("/course")
public class CourseController {
	private static Logger log = LoggerFactory.getLogger(CourseController.class);

	private CourseService courseService;

	@Autowired
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String viewCourse(@RequestParam("courseId") Integer courseId, Model model) {
		log.debug("In viewCourse courseId = {}", courseId);
		Course course = courseService.getCourseById(courseId);
		model.addAttribute(course);
		return "course_overview";
	}
	
	@RequestMapping(value = "/view2/{courseId}")
	public String viewCourse2(@PathVariable Integer courseId, Model model) {
		log.debug("In viewCourse courseId = {}", courseId);
		Course course = courseService.getCourseById(courseId);
		model.addAttribute(course);
		return "course_overview";
	}
	
	@RequestMapping("/view3")
	public String viewCourse3(HttpServletRequest request) {
		String courseId = request.getParameter("courseId");
		log.debug("In viewCourse courseId = {}", courseId);
		Course course = courseService.getCourseById(Integer.parseInt(courseId));
		request.setAttribute("course", course);
		return "course_overview";
	}
	
	@RequestMapping(value="/admin", method = RequestMethod.GET, params = "add")
	public String createCourse(){
		return "course_admin/edit";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String doSave(@ModelAttribute Course course) {
		log.debug("Info of Course:");
		log.debug(ReflectionToStringBuilder.toString(course));
		
		//在此进行业务操作，比如数据库持久化
		course.setCourseId(123);
		return "forward:view2/" + course.getCourseId();
	}
	
	/**
	 * 显示文件上传页面
	 * @param multi
	 * @return
	 */
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public String showUploadPage(@RequestParam(value= "multi", required = false) Boolean multi){	
		if(multi != null && multi){
			return "course_admin/multifile";	
		}
		return "course_admin/file";		
	}
	
	/**
	 * 单文件上传操作
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/doUpload", method=RequestMethod.POST)
	public String doUploadFile(@RequestParam("file") MultipartFile file) throws IOException{
		
		if(!file.isEmpty()){
			log.debug("Process file: {}", file.getOriginalFilename());
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File("c:\\temp\\imooc\\", System.currentTimeMillis()+ file.getOriginalFilename()));
		}
		
		return "success";
	}
	
	@RequestMapping(value = "/doupload3", method = RequestMethod.POST)
	public String doUploadFile3(@RequestParam("file") MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			log.debug("Process file: {}" + file.getOriginalFilename());
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File("e:\\temp\\immoc\\", System.currentTimeMillis() + file.getOriginalFilename()));
		}
		return "success";
	}
	
	/**
	 * 多文件上传
	 * @param multiRequest
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/doUpload2", method=RequestMethod.POST)
	public String doUploadFile2(MultipartHttpServletRequest multiRequest) throws IOException{
		
		Iterator<String> filesNames = multiRequest.getFileNames();
		while(filesNames.hasNext()){
			String fileName =filesNames.next();
			MultipartFile file =  multiRequest.getFile(fileName);
			if(!file.isEmpty()){
				log.debug("Process file: {}", file.getOriginalFilename());
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File("c:\\temp\\imooc\\", System.currentTimeMillis()+ file.getOriginalFilename()));
			}
			
		}
		
		return "success";
	}
}
