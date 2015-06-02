package com.shofuku.accsystem.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.hibernate.Session;

import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.domain.security.Module;
import com.shofuku.accsystem.domain.security.Role;
import com.shofuku.accsystem.utils.HibernateUtil;

public class UserRoleHelper {
	
	SecurityManager securityManager = new SecurityManager();

	public String parseModulesGrantedListToString(List modulesGrantedList) {
		
		Iterator itr = modulesGrantedList.iterator();
		String roleAccessString="";
		while(itr.hasNext()) {
			if(roleAccessString.equalsIgnoreCase("")) {
				roleAccessString = (String) itr.next(); 
			}else {
				roleAccessString = roleAccessString + ","+(String) itr.next();
			}
		}
		
		return roleAccessString;
	}
	
	public boolean isUserAuth(String roleId,Role userRole,List modulesList) {
		Map allowedRoles = parseModulesListToMap(rolesAccessStringToGrantedList(userRole, parseModulesListToMap(modulesList)));
	
		if(allowedRoles.get(Integer.valueOf(roleId))==null) {
			return false;
		}else {
			 return true;
		}
	}
	
	public List<Module> rolesAccessStringToGrantedList(Role role,Map modulesGrantedMap) {
		
		String[] roleSplit;
		roleSplit = role.getRoleAccessString().split(",");
		
		List<Module> modulesGrantedList = new ArrayList<Module>();
		
		for(int x=0;x<roleSplit.length;x ++) {
			modulesGrantedList.add( new Module(Integer.valueOf(roleSplit[x]), (String)modulesGrantedMap.get(Integer.valueOf(roleSplit[x])) ));
		}
		
		return sortModuleList(modulesGrantedList);
	}
	
	public Map parseModulesListToMap(List modulesNotGrantedList) {
		
		Map modulesGrantedMap = new HashMap();
		
		Iterator itr = modulesNotGrantedList.iterator();
		while(itr.hasNext()) {
			Module module = (Module) itr.next();
			modulesGrantedMap.put(module.getModuleId(), module.getModuleName());
			
		}
		return modulesGrantedMap;
	}
	

	public List loadModules() throws IndexOutOfBoundsException {
		Session session = getSession();
		
		List modulesList = new ArrayList<>();
			modulesList = securityManager.listAlphabeticalAscByParameter(Module.class, "moduleName", session);
	
		return modulesList;
	}
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public List removeGrantedModulesToAvailableModulesList(
			List modulesGrantedList,
			List modulesList) {
		
		Map modulesMap = new HashMap();
		Map grantedModulesMap = new HashMap();
		
		Iterator modulesItr = modulesList.iterator();
		while(modulesItr.hasNext()) {
			Module module = (Module)modulesItr.next();
			modulesMap.put(module.getModuleId(), module);
		}
		
		modulesItr = modulesGrantedList.iterator();
		while(modulesItr.hasNext()) {
			Module module = (Module)modulesItr.next();
			modulesMap.remove(module.getModuleId());
		}
		
		//sort the module List
		modulesList= new ArrayList();
		modulesList.addAll(modulesMap.values());
		modulesList= sortModuleList(modulesList);

		return modulesList;
		
	}
	
	private List sortModuleList (List inputList) {
		
		List sortedStringList = new ArrayList();
		Map modulesMap = new HashMap();
		
		Iterator modulesItr = inputList.iterator();
		
		while(modulesItr.hasNext()) {
			Module module = (Module)modulesItr.next();
			modulesMap.put(module.getModuleName(), module);
			sortedStringList.add(module.getModuleName());
		}
		
		Collections.sort(sortedStringList);
		
		Iterator sortItr = sortedStringList.iterator();
		inputList = new ArrayList();
		while(sortItr.hasNext()) {
			String moduleName  = (String) sortItr.next();
			inputList.add(modulesMap.get(moduleName));
		}
		
		return inputList;
	}
	
}
