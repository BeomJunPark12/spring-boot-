package com.study.board.controller;

import com.study.board.entity.Board2;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String boardWrite() {
        return "boardWrite";
    }

    @PostMapping("/board/write")
    public String boardWritedo(Board2 board2, MultipartFile file, Model model) throws Exception{

        boardService.write(board2, file);

        model.addAttribute("message", "글이 등록되었습니다.");
        model.addAttribute("url", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(String searchKeyword, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board2> list = null;

        if (searchKeyword == null) {
             list = boardService.boardlist(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }



        int currentPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(currentPage - 4, 1);
        int endPage = Math.min(currentPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardList";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {
        model.addAttribute("board2", boardService.boardView(id));

        return "boardView";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {
        boardService.boardDelete(id);

        model.addAttribute("message", "글이 삭제되었습니다.");
        model.addAttribute("url", "/board/list");

        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board2", boardService.boardView(id));

        return "boardModify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board2 board2, MultipartFile file, Model model) throws Exception {

        Board2 boardUpdate = boardService.boardView(id);

        boardUpdate.setTitle(board2.getTitle());
        boardUpdate.setContent(board2.getContent());

        boardService.write(boardUpdate, file);

        model.addAttribute("message", "글이 수정되었습니다.");
        model.addAttribute("url", "/board/modify/"+id);

        return "message";
    }
}
