package com.mugisha.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mugisha.models.Product;
import com.mugisha.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository pr;

	public Iterable<Product> getAllProducts() {
		return pr.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	public void addProduct(Product p) {
		pr.save(p);
	}

	public Product findById(int id) {
		return pr.findById(id).get();
	}

	public void deleteProduct(int id) {
		pr.deleteById(id);
	}
}
