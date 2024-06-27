package com.mugisha.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDto {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	@NotEmpty(message = "The name is Required")
	private String name;

	@Size(min = 10, message = "The description should be at least 10 characters")
	@Size(max = 2000, message = "The description can not exceed 2000 characters")
	private String description;

	@NotEmpty(message = "The brand is Required")
	private String brand;

	@NotEmpty(message = "The category is Required")
	private String category;

	@Min(0)
	private double price;

	private MultipartFile imageFile;

}
