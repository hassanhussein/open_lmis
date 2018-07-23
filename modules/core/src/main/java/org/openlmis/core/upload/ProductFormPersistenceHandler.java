package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.ProductForm;
import org.openlmis.core.service.ProductFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductFormPersistenceHandler extends AbstractModelPersistenceHandler {

    ProductFormService productFormService;

    @Autowired
    public ProductFormPersistenceHandler(ProductFormService productFormService) {
        this.productFormService = productFormService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return productFormService.getExisting((ProductForm) record);
    }

    @Override
    protected void save(BaseModel modelClass) {
        productFormService.save((ProductForm) modelClass);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.product.form";
    }
}
