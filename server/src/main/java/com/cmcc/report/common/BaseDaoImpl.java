package com.cmcc.report.common;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.report.util.Page;





@NoRepositoryBean
public class BaseDaoImpl<T,ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID>  {
	static Logger logger = Logger.getLogger(BaseDaoImpl.class);
    private final EntityManager em;
    
	public BaseDaoImpl(final JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		
		super(entityInformation, entityManager);
		this.em = entityManager;
	}
	
	public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getMetadata(domainClass, em), em); 
    }

	@Transactional
	public Page<T> getAllPage(String jpql, List<Object> params, int pageSize,
			int pageIndex) {
		Query query = em.createQuery(jpql);
		if(params!=null && params.size()!=0){
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i+1, params.get(i));
			}
		}
		Page<T> page=new Page<T>();
		int count=query.getResultList().size();
		page.setReultAllCount(count);
		query.setMaxResults(pageSize);
		page.setPageSize(pageSize);
		int firstResult = (pageIndex - 1) * pageSize;
		page.setPageIndex(pageIndex);
		query.setFirstResult(firstResult);
		@SuppressWarnings("unchecked")
		List<T> lis=query.getResultList();
		page.setList(lis);
		return page;
	}
	@Transactional
	public List<T> findAllList(String jpql, List<Object> params) {
		Query query = em.createQuery(jpql);
		if(params!=null && params.size()!=0){
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i+1, params.get(i));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> lis=query.getResultList();
		return lis;
	}
	
	@Transactional
	@Override
	public void batchDelete(List<T> entity) {
		if(entity==null ||entity.size()==0){
			return;
		}
		this.deleteInBatch(entity);
	}
	
	@Transactional
	@Override
	public void batchUpdate(List<T> entity,int leap) {
		int leaps=100;
		if(leap!=0){
			leaps=leap;
		}
		for (int i = 0; i < entity.size(); i++) {
			em.merge(entity.get(i));
			if(i%leaps==0){
				em.flush();
				em.clear();
			}
		}
	}
	@Transactional
	@Override
	public void batchAdd(List<T> entity,int leap) {
		int leaps=100;
		if(leap!=0){
			leaps=leap;
		}
		if(entity!=null && entity.size()!=0){
			for (int i = 0; i < entity.size(); i++) {
				em.persist(entity.get(i));
				if(i%leaps==0){
					em.flush();
					em.clear();
				}
			}
		}
		
	}
	
	@Transactional
	@Override
	public void batchAddorUpdate(List<T> entity, List<String> params,String keyName,int leap) throws ClassNotFoundException, Exception {
		if(entity!=null && entity.size()!=0){
			int leaps=100;
			if(leap!=0){
				leaps=leap;
			}
			String name=entity.get(0).getClass().getName();
			String jpql="select e from "+name+" e where 1=1";
			for (int i = 0; i < params.size(); i++) {
				jpql+=" and "+params.get(i)+"=:"+params.get(i);
			}
			Query qy= em.createQuery(jpql);
			/*ObjectUtil ob=new ObjectUtil();
			for (int i = 0; i < entity.size(); i++) {
				ob.setBean(entity.get(i));
				Map<String, Property> po=ob.getFPropertyForMap();
				for (int j = 0; j < params.size(); j++) {
					qy.setParameter(name, po.get(params.get(j)));
				}
				Object oup=qy.getSingleResult();
				if(qy.getSingleResult()!=null){
					ObjectUtil on=new ObjectUtil();
					on.setBean(oup);
					Property pot=po.get(keyName);
					pot.setValue(on.getProperty(keyName));
					ob.setProperty(po);
					em.merge(ob.getBean());
				}else{
					em.persist(entity.get(i));
				}
				if(i%leaps==0){
					em.flush();
					em.clear();
				}
				
			}*/
		}
	}
	@Transactional
	@Override
	public void batchDelete(String jpql, List<Object> params) {
		Query query=em.createQuery(jpql);
		if(params!=null && params.size()!=0){
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i+1, params.get(i));
			}
		}
		query.executeUpdate();
		
	}

	@Override
	public Object findForMap(String jpql, Object[] params) {
		Query query=em.createQuery(jpql);
		if(params!=null){
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i+1, params[i]);
			}
		}
		return query.getSingleResult();
	}

	@Override
	public Object update(Object o) {
		return em.merge(o);
	}
	public Object[] findForObject(String sql,Object[] params) {
		try{
			Query query=em.createNativeQuery(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i+1, params[i]);
				}
			}
			Object obj = query.getSingleResult();
			return  (Object[])obj;
		}catch(Exception e){
			return null;
		}
	}

  //得到数量
	@Transactional
	public Integer getCount(String jpql, List<Object> params) {
		Query query = em.createQuery(jpql);
		if(params!=null && params.size()!=0){
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i+1, params.get(i));
			}
		}
		long count=((Long)query.getSingleResult()).longValue();
		return (int)count;
	}

	@Override
	public double getAvg(String jpql, List<Object> params) {
		  double avg=0;
		  Query query  = em.createQuery(jpql);
		  if(query!=null){
		    Iterator it = query.getResultList().iterator();
		    while(it.hasNext()){
		        Object[] obj = (Object[])it.next();
		        if(params.get(0).equals(obj[0]))
		        	if(obj[1]!=null)
		        	avg=(Double)obj[1];
		        
		    }
		    }
		    return avg;
	}


	@Override
	public int getAvgPass(String jpql, List<Object> params) {
		  Query query  = em.createQuery(jpql);
		  long count=0;
		  try{
			  count = ((Long)query.getSingleResult()).longValue();
		  } catch (NoResultException nre){
				 
			  }

		  return (int)count;
	}
	public int getBeatNum(String jpql,List<Object> obj){
		Query query = em.createQuery(jpql);
		long count = 0;
		try{
			 count = ((Long)query.getSingleResult()).longValue();
		  } catch (NoResultException nre){
			  }
		 return (int)count;
	}
	
	@Override
	public int update(String jpql,List<Object> params) {
		 Query query = this.em.createNativeQuery(jpql);
		 if(params!=null && params.size()!=0){
				for (int i = 0; i < params.size(); i++) {
					query.setParameter(i+1, params.get(i));
				}
			}
         return  query.executeUpdate();
	}
}
