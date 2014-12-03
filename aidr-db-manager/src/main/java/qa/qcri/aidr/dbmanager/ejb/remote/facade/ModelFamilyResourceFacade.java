/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;
import javax.ejb.Remote;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;

/**
 *
 * @author Imran
 */
@Remote
public interface ModelFamilyResourceFacade extends CoreDBServiceFacade<ModelFamily, Long>{
    public List<ModelFamilyDTO> getAllModelFamilies() throws PropertyNotSetException;;
    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) throws PropertyNotSetException;
    public ModelFamilyDTO getModelFamilyByID(Long id) throws PropertyNotSetException;
    public ModelFamilyDTO addCrisisAttribute(ModelFamilyDTO modelFamily) throws PropertyNotSetException;
    
    //Clien to fix: return type chagned from void to boolean
    public boolean deleteModelFamily(Long modelFamilyID) throws PropertyNotSetException;
    
    //TODO for Koushik
    //public List<TaggersForCodes> getTaggersByCodes(List<String> codes);
    
}
