package com.project.product.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.project.inquire.domain.InquireDTO;
import com.project.inquire.service.InquireService;
import com.project.product.domain.CategoryDTO;
import com.project.product.domain.ProductDTO;
import com.project.product.domain.ProductFileDTO;
import com.project.product.service.CategoryService;
import com.project.product.service.ProductFileService;
import com.project.product.service.ProductService;
import com.project.review.domain.ReviewBoardDTO;
import com.project.review.service.ReviewService;
import com.project.utils.FileUploadUtils;

import net.sf.json.JSONArray;

@Controller
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private InquireService inquireService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductFileService productFileService;
	
	@Resource(name = "uploadPath")
	private String uploadPath;

	// 수정 삭제는 상품등록자만 가능하게(비지니스 로직)

	// 상품 게시글 작성폼
	@RequestMapping(value = "/product/productWritePage", method = RequestMethod.GET)
	public String productWritePage(Model model) throws Exception {

		logger.info("상품 게시글 등록 productWriteForm -  Controller");

		List<CategoryDTO> category = categoryService.categoryList();

		model.addAttribute("categoryList", JSONArray.fromObject(category));

		return "/product/productWritePage";
	}

	// 상품 게시글 등록하기
	@RequestMapping(value = "/product/productWrite", method = RequestMethod.POST)
	public String productWrite(ProductDTO productDTO , ProductFileDTO productFileDTO , MultipartFile file) throws Exception {

		logger.info("상품 게시글 등록하기 productWrite - Controller : {}", productDTO);
		
		productService.productWrite(productDTO);
		
		logger.info("상품 등록 완료. 사진 등록 시작");
		
		logger.info("상품번호 : {}", productDTO.getPno());
		
		String imgUploadPath = uploadPath + File.separator + "imgUpload";
		String ymdPath = FileUploadUtils.calcPath(imgUploadPath);
		String fileName = null;
		
		if ( file.getOriginalFilename() != null && file.getOriginalFilename() != "" ) {
			
			fileName = FileUploadUtils.fileUpload(imgUploadPath, file.getOriginalFilename(), file.getBytes(), ymdPath);
			
		} else {
			
			fileName = "images" + File.separator + "imageNone.jpg";
			
		}
		
		// DB에 저장될 파일 경로, 이 경로는 나중에 img태그를 이용하여 클라이언트한테 이미지를 보여줄 때 중요하게 사용이 된다.
		String storedFileName = File.separator + "imgUpload" + ymdPath + File.separator + fileName;
		String storedThumbNail = File.separator + "imgUpload" + ymdPath + File.separator + "thumbs" + File.separator + "thumbnail_" + fileName;
		
		productFileDTO = new ProductFileDTO(file.getOriginalFilename(), storedFileName, storedThumbNail);
		
		logger.info("제품 파일 업로드 productWrite - controller : {}", productFileDTO);
		
		productFileService.productFileUpload(productFileDTO);

		return "redirect:/product/productList";

	}

	// 상품 게시글 수정
	@RequestMapping(value = "/product/productUpdate", method = RequestMethod.POST)
	public String productUpdate(ProductDTO productDTO) throws Exception {

		logger.info("상품 게시글 수정 productUpdate - Controller");

		productService.productUpdate(productDTO);

		return "";
	}

	// 상품 게시글 삭제
	@RequestMapping(value = "/product/productDelete", method = RequestMethod.POST)
	public String productDelete(int pno) throws Exception {

		logger.info("상품 게시글 삭제 productDelete - Controller");

		productService.productDelete(pno);

		return "redirect:/product/main";
	}

	// 상품 상세조회
	@RequestMapping(value = "/product/productView", method = RequestMethod.GET)
	public void productDetail(int pno, Model model, ProductDTO productDTO) throws Exception {

		logger.info("상품 게시글 상세 조회 productView - Controller");

		//--------------------------------- 로직 나중 구현 예정--------------------------------------------

		List<InquireDTO> inquireList = inquireService.inquireList();

		List<ReviewBoardDTO> reviewList = reviewService.reviewList();

		productDTO = productService.productView(pno);

		model.addAttribute("productDTO", productDTO);

		model.addAttribute("inquireList", inquireList);

		model.addAttribute("reviewList", reviewList);

	}

	// 상품 게시글 목록
	@RequestMapping(value = "/product/productList", method = RequestMethod.GET)
	public void ProductList(Model model) throws Exception {

		logger.info("상품 게시글 목록 조회 ProductList - Controller");

		List<ProductDTO> productList = productService.productList();

		model.addAttribute("productList", productList);
	}

}
