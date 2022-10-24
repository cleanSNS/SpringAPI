package cleanbook.com.service;

import cleanbook.com.exception.exceptions.MyException;
import cleanbook.com.util.CommonUtils;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadFiles(String category, List<MultipartFile> multipartFiles) {
        List<String> fileUrlList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            validateFileExists(multipartFile);

            String fileName = CommonUtils.createFileName(category, multipartFile.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {
                byte[] bytes = IOUtils.toByteArray(inputStream);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                objectMetadata.setContentLength(bytes.length);

                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                fileUrlList.add(amazonS3Client.getUrl(bucketName, fileName).toString());
            } catch (IOException e) {
                throw new MyException("파일 업로드에 실패했습니다.");
            }
        }

        return fileUrlList;
    }

    public void deleteFiles(List<String> filenameList) {
        for (String filename : filenameList) {
            filename = filename.substring(filename.indexOf(".com"));
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, filename));
        }
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new MyException("파일이 비어있습니다.");
        }
    }


}