package com.core.airbnbclonebackend.repository;

import com.core.airbnbclonebackend.entity.FileDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, UUID> {

}
