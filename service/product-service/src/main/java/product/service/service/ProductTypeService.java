package product.service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.product.IProductTypeService;
import com.entity.TProductType;
import com.mapper.TProductTypeMapper;
import common.base.BaseServiceImpl;
import common.base.IBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: LiuXing
 * @DATE: 2020/5/27
 */
@Service
@Component
public class ProductTypeService extends BaseServiceImpl<TProductType> implements IProductTypeService{


    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }


}
