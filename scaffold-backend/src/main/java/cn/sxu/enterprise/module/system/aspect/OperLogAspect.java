package cn.sxu.enterprise.module.system.aspect;

import cn.sxu.enterprise.common.security.model.LoginUser;
import cn.sxu.enterprise.common.web.annotation.OperLog;
import cn.sxu.enterprise.module.system.entity.SysOperLog;
import cn.sxu.enterprise.module.system.service.SysOperLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class OperLogAspect {

    private final SysOperLogService sysOperLogService;
    private final ObjectMapper objectMapper;

    public OperLogAspect(SysOperLogService sysOperLogService, ObjectMapper objectMapper) {
        this.sysOperLogService = sysOperLogService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(operLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperLog operLog) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = null;
        Throwable throwable = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            throwable = ex;
            throw ex;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            saveOperLog(joinPoint, operLog, result, throwable, costTime);
        }
    }

    private void saveOperLog(ProceedingJoinPoint joinPoint,
                             OperLog operLog,
                             Object result,
                             Throwable throwable,
                             long costTime) {
        try {
            HttpServletRequest request = getCurrentRequest();

            SysOperLog log = new SysOperLog();
            log.setTitle(operLog.title());
            log.setBusinessType(operLog.businessType());
            log.setMethod(getMethodName(joinPoint));
            log.setRequestMethod(request == null ? null : request.getMethod());
            log.setOperatorType("WEB");
            log.setOperName(getCurrentUsername());
            log.setOperUrl(request == null ? null : request.getRequestURI());
            log.setOperIp(getClientIp(request));
            log.setOperLocation(getOperLocation(log.getOperIp()));
            log.setOperParam(truncate(toJson(getSafeArgs(joinPoint.getArgs())), 2000));

            if (throwable == null) {
                log.setStatus(0);
                log.setJsonResult(truncate(toJson(result), 2000));
            } else {
                log.setStatus(1);
                log.setErrorMsg(truncate(throwable.getMessage(), 2000));
            }

            log.setOperTime(LocalDateTime.now());
            log.setCostTime(costTime);
            log.setCreateBy(log.getOperName());
            log.setCreateTime(LocalDateTime.now());
            log.setDeleted(0);

            sysOperLogService.save(log);
        } catch (Exception ignored) {
            // 操作日志记录失败不能影响正常接口返回
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        return attributes.getRequest();
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getName() + "()";
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return "anonymous";
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof LoginUser loginUser) {
            return loginUser.getUsername();
        }

        String name = authentication.getName();
        return StringUtils.hasText(name) ? name : "anonymous";
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    private String getOperLocation(String ip) {
        if (!StringUtils.hasText(ip)) {
            return "未知";
        }

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equalsIgnoreCase(ip)) {
            return "本地开发环境";
        }

        return "未知";
    }

    private List<Object> getSafeArgs(Object[] args) {
        List<Object> safeArgs = new ArrayList<>();

        if (args == null) {
            return safeArgs;
        }

        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                continue;
            }

            if (arg instanceof HttpServletResponse) {
                continue;
            }

            safeArgs.add(arg);
        }

        return safeArgs;
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength);
    }
}