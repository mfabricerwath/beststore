package com.mugisha.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mugisha.models.Product;
import com.mugisha.models.ProductDto;
import com.mugisha.services.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService ps;

	@GetMapping({ "", "/" })
	public String getProducts(Model model) {
		Iterable<Product> pr = ps.getAllProducts();
		model.addAttribute("products", pr);
		return "products/index";
	}
	
	@GetMapping( "/fabrice")
	public String goToBootStrap() {
		return "01bootstrap";
	}

	@GetMapping("/create")
	public String showCreatePage(Model m) {
		ProductDto pd = new ProductDto();
		m.addAttribute("productDto", pd);
		return "products/createProduct";
	}

	@PostMapping("/create")
	public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {

		if (productDto.getImageFile().isEmpty()) {

			result.addError(new FieldError("productDto", "imageFile", "The image file is required"));
		}
		if (result.hasErrors()) {
			return "products/createProduct";
		}
		// save the image

		MultipartFile image = productDto.getImageFile();
		Date createdAt = new Date();
		String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = image.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
		}

		Product product = new Product();
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setPrice(productDto.getPrice());
		product.setDescription(productDto.getDescription());
		product.setCreatedAt(createdAt);
		product.setImageFileName(storageFileName);

		ps.addProduct(product);

		return "redirect:/products";
	}

	@GetMapping("/edit")
	public String editProduct(Model model, @RequestParam int id) {
		try {
			Product product = ps.findById(id);
			model.addAttribute("product", product);

			ProductDto pd = new ProductDto();

			pd.setName(product.getName());
			pd.setBrand(product.getBrand());
			pd.setCategory(product.getCategory());
			pd.setPrice(product.getPrice());
			pd.setDescription(product.getDescription());

			model.addAttribute("productDto", pd);

		} catch (Exception ex) {
			System.out.println("Exception" + ex.getMessage());
			return "redirect:/products";
		}

		return "products/editProduct";
	}

	@PostMapping("/edit")
	public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto,
			BindingResult result) {
	try {
		Product product=ps.findById(id);
		model.addAttribute("product", product);
		
		if(result.hasErrors()) {
			return "products/editProduct";
		}
		
		if(!productDto.getImageFile().isEmpty()) {
			//delete old image
			String uploadDir = "public/images/";
			Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
			
		try {
			Files.delete(oldImagePath);
		}catch(Exception ex) {
			System.out.println("Exception for deletion of the image :" + ex.getMessage());
		}
		// save new image file
		MultipartFile image = productDto.getImageFile();
		Date createdAt= new Date();
		String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
		
		try(InputStream inputStream = image.getInputStream()) {
			Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
		}
		
		product.setImageFileName(storageFileName);
		
		}
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setPrice(productDto.getPrice());
		product.setDescription(productDto.getDescription());
		
		ps.addProduct(product);
		
	}catch(Exception ex) {
		System.out.println("Exception : "+ ex.getMessage());
	}

		
		return "redirect:/products";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(@RequestParam int id) {
		try {
			Product product = ps.findById(id);
			
			// delete the product image
			
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());
			try {
				Files.delete(imagePath);
			}catch(Exception ex) {
				System.out.println("Exception the product image not deleted: " + ex.getMessage() );
			}
			
			ps.deleteProduct(id);
		}catch(Exception ex) {
			System.out.println("Exception on Getting a product for deletion : " + ex.getMessage());
		}
		return "redirect:/products";
	}
	
	
}
