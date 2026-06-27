package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.security.model.LoginUser;
import cn.sxu.enterprise.module.system.entity.SysFile;
import cn.sxu.enterprise.module.system.mapper.SysFileMapper;
import cn.sxu.enterprise.module.system.service.SysFileService;
import cn.sxu.enterprise.module.system.vo.SysFilePageQuery;
import cn.sxu.enterprise.module.system.vo.SysFilePageVO;
import cn.sxu.enterprise.module.system.vo.SysFileUploadVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class SysFileServiceImpl implements SysFileService {

    private final SysFileMapper sysFileMapper;

    @Value("${enterprise.file.local-upload-path}")
    private String localUploadPath;

    @Value("${enterprise.file.access-prefix:/files}")
    private String accessPrefix;

    public SysFileServiceImpl(SysFileMapper sysFileMapper) {
        this.sysFileMapper = sysFileMapper;
    }

    @Override
    public SysFileUploadVO upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(500, "上传文件不能为空");
        }

        String originalName = file.getOriginalFilename();
        if (!StringUtils.hasText(originalName)) {
            originalName = "unknown";
        }

        String fileType = getFileType(originalName);
        String dateFolder = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String storedFileName = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.hasText(fileType)) {
            storedFileName = storedFileName + "." + fileType;
        }

        String objectName = dateFolder + "/" + storedFileName;

        Path rootPath = Paths.get(localUploadPath).toAbsolutePath().normalize();
        Path targetPath = rootPath.resolve(objectName).normalize();

        if (!targetPath.startsWith(rootPath)) {
            throw new BusinessException(500, "非法文件路径");
        }

        try {
            Files.createDirectories(targetPath.getParent());

            byte[] bytes = file.getBytes();
            String md5 = DigestUtils.md5DigestAsHex(bytes);

            Files.write(targetPath, bytes);

            LoginUser loginUser = getCurrentLoginUser();

            SysFile sysFile = new SysFile();
            sysFile.setFileName(storedFileName);
            sysFile.setOriginalName(originalName);
            sysFile.setFileType(fileType);
            sysFile.setFileSize(file.getSize());
            sysFile.setStorageType("LOCAL");
            sysFile.setBucketName(null);
            sysFile.setObjectName(objectName);
            sysFile.setUrl(buildAccessUrl(objectName));
            sysFile.setMd5(md5);
            sysFile.setUploadUserId(loginUser == null ? null : loginUser.getUserId());
            sysFile.setStatus(0);
            sysFile.setCreateBy(loginUser == null ? null : loginUser.getUsername());
            sysFile.setCreateTime(LocalDateTime.now());
            sysFile.setDeleted(0);

            sysFileMapper.insert(sysFile);

            return SysFileUploadVO.fromEntity(sysFile);
        } catch (IOException e) {
            throw new BusinessException(500, "文件上传失败");
        }
    }

    @Override
    public PageResult<SysFilePageVO> pageFiles(SysFilePageQuery query) {
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(query.getOriginalName()), SysFile::getOriginalName, query.getOriginalName())
                .eq(StringUtils.hasText(query.getFileType()), SysFile::getFileType, query.getFileType())
                .eq(StringUtils.hasText(query.getStorageType()), SysFile::getStorageType, query.getStorageType())
                .eq(query.getStatus() != null, SysFile::getStatus, query.getStatus())
                .orderByDesc(SysFile::getCreateTime)
                .orderByDesc(SysFile::getId);

        Page<SysFile> page = sysFileMapper.selectPage(
                new Page<>(query.getPageNo(), query.getPageSize()),
                wrapper
        );

        List<SysFilePageVO> records = page.getRecords()
                .stream()
                .map(SysFilePageVO::fromEntity)
                .toList();

        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    private String getFileType(String originalName) {
        int index = originalName.lastIndexOf(".");
        if (index < 0 || index == originalName.length() - 1) {
            return "";
        }
        return originalName.substring(index + 1).toLowerCase();
    }

    private String buildAccessUrl(String objectName) {
        String prefix = accessPrefix.endsWith("/") ? accessPrefix.substring(0, accessPrefix.length() - 1) : accessPrefix;
        return prefix + "/" + objectName.replace("\\", "/");
    }

    private LoginUser getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser;
        }
        return null;
    }
}
