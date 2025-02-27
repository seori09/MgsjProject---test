package com.project.product.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.product.dao.CartDAO;
import com.project.product.domain.CartDTO;

@Service
public class CartServiceImpl implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private CartDAO cartDAO;
	
	//장바구니 등록
	@Override
	public void cartWrite(CartDTO cartDTO) throws Exception {
		
		logger.info("장바구니 등록 cartWrite - Service");
		
		cartDAO.cartWrite(cartDTO);
		
	}
	
	//장바구니 수정
	@Override
	public void cartUpdate(CartDTO cartDTO) throws Exception {
		
		logger.info("장바구니 수정 cartUpdate - Service");
		
		cartDAO.cartUpdate(cartDTO);
		
	}
	
	//장바구니 삭제
	@Override
	public void cartDelete(int cartNum) throws Exception {
		
		logger.info("장바구니 삭제 cartDelete - Service");
		
		cartDAO.cartDelete(cartNum);
		
	}
	
	//장바구니 목록
	@Override
	public List<CartDTO> cartList(CartDTO cartDTO) throws Exception {
		
		logger.info("장바구니 목록 cartList - Service");
		
		
		return cartDAO.cartList(cartDTO);
	}
}
