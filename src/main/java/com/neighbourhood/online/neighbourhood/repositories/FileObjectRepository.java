package com.neighbourhood.online.neighbourhood.repositories;

import com.neighbourhood.online.neighbourhood.models.FileObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileObjectRepository  extends CrudRepository<FileObject,String> {
}
