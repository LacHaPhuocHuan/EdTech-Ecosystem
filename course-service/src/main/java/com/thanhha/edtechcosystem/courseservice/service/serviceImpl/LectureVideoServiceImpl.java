package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.LectureVideoDto;
import com.thanhha.edtechcosystem.courseservice.entity.LectureVideo;
import com.thanhha.edtechcosystem.courseservice.exception.StorageException;
import com.thanhha.edtechcosystem.courseservice.repository.LectureRepository;
import com.thanhha.edtechcosystem.courseservice.repository.LectureVideoRepository;
import com.thanhha.edtechcosystem.courseservice.rest.ILectureVideoRest;
import com.thanhha.edtechcosystem.courseservice.service.ILectureVideoService;
import com.thanhha.edtechcosystem.courseservice.utils.StorageProperties;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(rollbackFor = IOException.class)
public class LectureVideoServiceImpl implements ILectureVideoService {
    private final LectureVideoRepository lectureVideoRepository;
    private final ModelMapper modelMapper;
    private final LectureRepository lectureRepository;
    private final Path rootLocation;

    public LectureVideoServiceImpl(LectureVideoRepository lectureVideoRepository, ModelMapper modelMapper, StorageProperties properties, LectureRepository lectureRepository) {
        this.lectureVideoRepository = lectureVideoRepository;
        this.modelMapper = modelMapper;
        this.lectureRepository = lectureRepository;
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public LectureVideoDto getById(Long id) {
        //todo Cache
        return modelMapper.map(
                lectureVideoRepository.findById(id).orElseThrow(NotFoundException::new),
                LectureVideoDto.class
        );
    }

    @Override
    public LectureVideoDto store(MultipartFile file, String id) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(UUID.randomUUID().toString().substring(10)+file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);

            }
            var video= LectureVideo.builder()
                    .lecture(lectureRepository.findById(id).orElseThrow(NotFoundException::new))
                    .title(file.getOriginalFilename())
                    .uploadDate(new Date())
                    .url(destinationFile.toRealPath().toString())
                    .build();
            var saveVideo=lectureVideoRepository.save(video);

            return modelMapper.map(saveVideo, LectureVideoDto.class );

        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }
    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }


}
