package com.haivera.processcenter.common;

import org.apache.commons.lang.StringUtils;

public class IdCombine {

    /**
     * 合并系统id与用户id 或是系统id与用户组id
     * @param systemId
     * @param userIdOrGroupId
     * @return 系统id_用户id 或是 系统id_用户组id
     */
    public static String combineId(String systemId, String userIdOrGroupId){
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotEmpty(systemId)){
            sb.append(systemId).append("_");
        }
        sb.append(userIdOrGroupId);
        return sb.toString();
    }
}
