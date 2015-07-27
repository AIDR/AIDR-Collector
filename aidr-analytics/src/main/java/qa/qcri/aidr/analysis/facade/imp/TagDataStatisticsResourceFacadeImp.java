
package qa.qcri.aidr.analysis.facade.imp;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;

import qa.qcri.aidr.common.values.ReturnCode;

/**
 * Implements the operations for the interface TagDataStatisticsResourceFacade
 */
@Stateless(name = "TagDataStatisticsResourceFacadeImp")
public class TagDataStatisticsResourceFacadeImp implements TagDataStatisticsResourceFacade {

	private static Logger logger = Logger.getLogger(TagDataStatisticsResourceFacadeImp.class);
	
	@PersistenceContext(unitName = "qa.qcri.aidr.analysis-EJBS")
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		try {
			return em; 
		} catch (Exception e) {
			throw new HibernateException("getEntityManager failed");
		}
	}

	@Override
	public int setEntityManager(EntityManager em) {
		try {
			if (null == this.em) { 
				this.em = em;
				logger.info("EntityManager set to new value: " + this.em);
				return 1;
			} else 
				logger.info("Skipping setter, since EntityManager already initialized to :" + this.em);
			return 0;
		} catch (Exception e) {
			logger.error("EntityManager setting exception : " + em);
			logger.error("exception", e);
			throw new HibernateException("setEntityManager failed");
		}
	}

	@Override
	public Session getCurrentSession() {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error("exception: ", e);
			e.printStackTrace();
			throw new HibernateException("getCurrentSession failed");
		}
	}
	
	
	@Override
	public ReturnCode writeData(TagData tagData) {
		try {
			em.persist(tagData);
			System.out.println("Success in persisting tag data for: " + tagData.getCrisisCode() + ", " + tagData.getAttributeCode() 
					+ ", " + tagData.getLabelCode() + ", " + tagData.getTimestamp() + ", " + tagData.getGranularity() + ": " + tagData.getCount());
			return ReturnCode.SUCCESS;
		} catch (Exception e) {
			System.out.println("Failure in persisting tag data for: " + tagData.getCrisisCode() + ", " + tagData.getAttributeCode() 
					+ ", " + tagData.getLabelCode() + ", " + tagData.getTimestamp() + ", " + tagData.getGranularity() + ": " + tagData.getCount());
			e.printStackTrace();
			logger.error("exception: ", e);
			return ReturnCode.ERROR;
		}
	}

	@Override
	public TagData getSingleDataByPK(TagDataPK tagDataPK) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", tagDataPK.getCrisisCode()))
				.add(Restrictions.eq("timestamp", tagDataPK.getTimestamp()))
				.add(Restrictions.eq("granularity", tagDataPK.getGranularity()))
				.add(Restrictions.eq("attributeCode", tagDataPK.getAttributeCode()))
				.add(Restrictions.eq("labelCode", tagDataPK.getLabelCode()));
		criteria.add(criterion); 		
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			TagData obj = (TagData) criteria.uniqueResult();
			return obj;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TagData> getDataByCrisis(String crisisCode) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		criteria.add(Restrictions.eq("crisisCode", crisisCode)); 		
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TagData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
		criteria.add(criterion); 		
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<TagData> getDataByCrisisAttributeLabelGranularity(String crisisCode, String attributeCode, String labelCode, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.eq("labelCode", labelCode))
				.add(Restrictions.eq("granularity", granularity));
		criteria.add(criterion); 		
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TagData> getDataByGranularityInTimeWindow(String crisisCode, String attributeCode, String labelCode, 
															Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.eq("granularity", granularity))
				.add(Restrictions.eq("timestamp", timestamp));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
				
		criteria.add(criterion); 		
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<TagData> getDataAfterTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.ge("timestamp", timestamp));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
				
		criteria.add(criterion); 
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<TagData> getDataAfterTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
															Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.ge("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
		criteria.add(criterion); 
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<TagData> getDataBeforeTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.le("timestamp", timestamp));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
				
		criteria.add(criterion); 		
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<TagData> getDataBeforeTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
															Long timestamp, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.le("timestamp", timestamp))
				.add(Restrictions.eq("granularity", granularity));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
				
		criteria.add(criterion);
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public List<TagData> getDataInInterval(String crisisCode, String attributeCode, String labelCode, Long timestamp1, Long timestamp2) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisisCode", crisisCode))
				.add(Restrictions.eq("attributeCode", attributeCode))
				.add(Restrictions.ge("timestamp", timestamp1))
				.add(Restrictions.le("timestamp", timestamp2));
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
		criteria.add(criterion); 
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<TagData> getDataInIntervalWithGranularity(String crisisCode, String attributeCode, String labelCode, 
														  Long timestamp1, Long timestamp2, Long granularity) {
		Criteria criteria = getCurrentSession().createCriteria(TagData.class);
		Criterion criterion = Restrictions.eq("crisisCode", crisisCode);
				
		// Now add the optional non-null criteria
		if (attributeCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("attributeCode", attributeCode));
		}
		if (labelCode != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("labelCode", labelCode));
		}
		if (granularity != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.eq("granularity", granularity));
		}
		if (timestamp1 != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.ge("timestamp", timestamp1));
		}
		if (timestamp2 != null) {
			criterion = Restrictions.conjunction()
						.add(criterion)
						.add(Restrictions.le("timestamp", timestamp2));
		}	
		criteria.add(criterion); 
		System.out.println("Formed criteria: " + criteria.toString());
		try {
			List<TagData> objList = (List<TagData>) criteria.list();
			System.out.println("fetched list size = " + (objList != null ? objList.size() : "null"));
			return objList;
		} catch (HibernateException e) {
			//logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
}
