package com.piper.valley.models.service;

import com.piper.valley.forms.AddProductForm;
import com.piper.valley.models.domain.PhysicalProduct;
import com.piper.valley.models.domain.Product;
import com.piper.valley.models.domain.VirtualProduct;
import com.piper.valley.models.repository.BrandRepository;
import com.piper.valley.models.repository.CompanyRepository;
import com.piper.valley.models.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository){this.productRepository=productRepository;}

	@Override
	public Optional<Product> getProductById(Long id) {
		return Optional.ofNullable(productRepository.findOne(id));
	}

	@Override
	public Optional<Product> getProductByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public Optional<Product> getPriceBetween(Float start,Float end)
	{
		return productRepository.findByAveragePriceBetween(start,end);
	}

	@Override
	public Collection<Product>getAllProducts()
	{
		return productRepository.findAll();
	}

	@Override
	public Product addProduct(AddProductForm productForm) {
		Product product;
		if(productForm.getPhysicalProduct())
		{
			PhysicalProduct physicalProduct = new PhysicalProduct();
			physicalProduct.setLength(productForm.getLength());
			physicalProduct.setWidth (productForm.getWidth() );
			physicalProduct.setHeight(productForm.getHeight());
			physicalProduct.setWeight(productForm.getWeight());
			product = physicalProduct;
		}
		else {
			VirtualProduct virtualProduct = new VirtualProduct();
			virtualProduct.setSerial(productForm.getSerial());
			product = virtualProduct;
		}

		//Common Attributes
		product.setBrand(brandRepository.findOneById(productForm.getBrandId()).get());
		product.setCompany(companyRepository.findOneById(productForm.getCompanyId()).get());
		product.setName(productForm.getName());
		product.setAveragePrice(productForm.getAveragePrice());
		product.setDateTime(new Date());

		return productRepository.save(product);
	}
	public void incrementViews(Product product) {
			product.setView(product.getView()+1);
			productRepository.save(product);
	}
}
