package com.study.board.service;

import com.study.board.entity.Board2;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;


    // 글 작성 처리
    public void write(Board2 board2, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") +"\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board2.setFilename(fileName);
        board2.setFilepath("/files/" + fileName);

        boardRepository.save(board2);
    }

    // 글 리스트 처리
    public Page<Board2> boardlist(Pageable pageable) {
        return boardRepository.findAll(pageable);

    }

    // 글 리스트 검색 포함

    public Page<Board2> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시물 불러오기
    public Board2 boardView(Integer id) {
        return boardRepository.findById(id).get();
    }

    // 글 삭제 처리
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

}
