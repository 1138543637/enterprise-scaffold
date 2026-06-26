package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.module.system.entity.SysMenu;
import cn.sxu.enterprise.module.system.mapper.SysMenuMapper;
import cn.sxu.enterprise.module.system.service.SysMenuService;
import cn.sxu.enterprise.module.system.vo.SysMenuTreeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenuTreeVO> menuTree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus, 0);
        wrapper.orderByAsc(SysMenu::getParentId);
        wrapper.orderByAsc(SysMenu::getOrderNum);

        List<SysMenu> menus = this.list(wrapper);
        return buildTree(menus);
    }

    private List<SysMenuTreeVO> buildTree(List<SysMenu> menus) {
        Map<Long, SysMenuTreeVO> menuMap = new LinkedHashMap<>();

        for (SysMenu menu : menus) {
            menuMap.put(menu.getId(), SysMenuTreeVO.fromEntity(menu));
        }

        List<SysMenuTreeVO> roots = new ArrayList<>();

        for (SysMenu menu : menus) {
            SysMenuTreeVO current = menuMap.get(menu.getId());
            Long parentId = menu.getParentId();

            if (parentId == null || parentId == 0 || !menuMap.containsKey(parentId)) {
                roots.add(current);
            } else {
                SysMenuTreeVO parent = menuMap.get(parentId);
                parent.getChildren().add(current);
            }
        }

        return roots;
    }
}