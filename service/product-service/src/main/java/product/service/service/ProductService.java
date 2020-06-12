package product.service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.VO.ProductVO;
import com.api.product.IProductService;
import com.entity.TProduct;
import com.entity.TProductDesc;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.TProductDescMapper;
import com.mapper.TProductMapper;
import common.base.BaseServiceImpl;
import common.base.IBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @Author: LiuXing
 * @DATE: 2020/5/27
 */
@Service
@Component
public class ProductService extends BaseServiceImpl<TProduct> implements IProductService {


    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }


    /*@Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        //设置page页属性
        PageHelper.startPage(pageIndex,pageSize);
        System.out.println("pageIndex:"+pageIndex);
        System.out.println("pageSize:"+pageSize);
        //获取结果集
        List<TProduct> list = productMapper.list();
        //nabigatePages:分页栏页码数
        PageInfo<TProduct> pageInfo = new PageInfo<>(list, 3);
        pageInfo.setPages(pageSize);
        pageInfo.setSize(pageSize);
        return pageInfo;
    }*/

    @Override
    public Long add(ProductVO productVO) {
        //添加商品
        TProduct product = productVO.getProduct();
        product.setFlag(true);
        product.setCreateTime(new Date());
        product.setImage(product.getImage());
        product.setUpdateTime(product.getCreateTime());
        product.setUpdateUser(1L);
        product.setCreateUser(1L);
        product.setStock(100L);
        productMapper.insertSelective(product);
        //添加描述
        TProductDesc productDesc = new TProductDesc();
        productDesc.setpDesc(productVO.getProductDesc());
        productDesc.setProductId(product.getId());
        productDescMapper.insertSelective(productDesc);
        return product.getId();
    }
}
