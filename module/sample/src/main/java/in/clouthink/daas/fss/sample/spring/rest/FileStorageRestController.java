package in.clouthink.daas.fss.sample.spring.rest;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.repository.FileObjectRepository;
import in.clouthink.daas.fss.rest.ResultResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dz on 16/3/30.
 */
@Controller
@RequestMapping(value = "/api")
public class FileStorageRestController {

	@Autowired
	private FileObjectRepository fileObjectRepository;

	@RequestMapping(value = "/files", method = RequestMethod.GET)
	@ResultResponseBody
	public Page<FileObject> listFileObject(@RequestParam(value = "start", defaultValue = "0") int start,
										   @RequestParam(value = "limit", defaultValue = "10") int limit) {
		return fileObjectRepository.findAll(new PageRequest(start, limit));
	}

}
