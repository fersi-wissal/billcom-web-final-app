package com.billcom.app.controller;

import java.util.List;
import java.util.Set;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billcom.app.entity.Role;
import com.billcom.app.service.RoleService;

@RestController
@RequestMapping(value="role")

public class RoleController {
        private RoleService roleService;
        
        
        
        public RoleController(RoleService roleService) {
			this.roleService = roleService;
		}
        @PostMapping("/save")
        public Role saveRole( @RequestBody Role role){
        	return roleService.saveRole(role);
        }
		@GetMapping("/getList/{id}")
        public Set<Role> getRole(@PathVariable Long id){
        	return roleService.getListUser(id);
        }
    	@PutMapping("/addUserAccessRole/{id}")
    	public void upateUserAccessRole(@PathVariable Long id, @RequestBody List<String> roles) {
    		roleService.updateAccesRoleUsers(id, roles);
    	}

    	@PutMapping("/deleteRoleFromUser/{id}")
    	public void deleteRoleFromUsrt(@PathVariable Long id, @RequestBody String role) {
    		roleService.deleteUserRole(id, role);
    	}
}
