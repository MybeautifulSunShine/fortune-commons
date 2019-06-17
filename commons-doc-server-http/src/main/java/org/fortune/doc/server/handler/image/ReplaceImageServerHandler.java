package org.fortune.doc.server.handler.image;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.account.ImageDocThumbBean;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.server.handler.DocServerHandler;
import org.fortune.doc.server.util.ImageUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/6/16 14:13
 * @description:
 */
@Component
public class ReplaceImageServerHandler extends DocServerHandler {

    public ImageDocResult doReplace(HttpServletRequest request) {
        ImageDocResult result = new ImageDocResult();
        DocAccountBean accountBean = super.getAccount(request);
        if (accountBean == null) {
            result.buildFailed();
            result.buildCustomMsg("账号信息不对，请重新确认");
            return result;
        } else {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mreqeust = (MultipartHttpServletRequest)request;
                String filePath = mreqeust.getParameter(Constants.FILE_PATH_KEY);
                if (StringUtils.isEmpty(filePath)) {
                    result.buildCustomMsg("替换的文件路径为空");
                    result.buildFailed();
                    return result;
                }

                MultipartFile file = mreqeust.getFile(Constants.FILE_PATH_KEY);
                if (!file.isEmpty()) {
                    try {
                        String rootPath = super.getImageRootPath();
                        this.checkRootPath(rootPath);
                        String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
                        String srcfilePathName = filePath.substring(0, filePath.lastIndexOf("."));
                        String realPath = this.getRealPath(filePath);
                        File oldFile = new File(realPath);
                        if (!oldFile.exists() || !oldFile.isFile()) {
                            result.buildCustomMsg("替换的文件不存在");
                            result.buildFailed();
                            return result;
                        }

                        oldFile.delete();
                        file.transferTo(oldFile);
                        List<ImageDocThumbBean> thumbBeans = accountBean.getThumbConfig();
                        if (thumbBeans != null) {
                            Iterator thumbBean = thumbBeans.iterator();

                            while(thumbBean.hasNext()) {
                                ImageDocThumbBean thumb = (ImageDocThumbBean)thumbBean.next();
                                String thumbFilePath = this.getRealPath(srcfilePathName + thumb.getSuffix() + "." + fileExt);
                                File thumbFile = new File(thumbFilePath);
                                if (thumbFile.exists()) {
                                    ImageUtils.ratioZoom2(oldFile, thumbFile, thumb.getRatio());
                                }
                            }
                        }

                        result.setFilePath("");
                        result.buildSuccess();
                    } catch (Exception ex) {
                        result.buildFailed();
                    }
                } else {
                    result.buildFailed();
                }
            }

            result.buildFailed();
            return result;
        }
    }

}
