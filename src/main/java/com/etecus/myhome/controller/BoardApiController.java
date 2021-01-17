package com.etecus.myhome.controller;

import java.util.List;

import com.etecus.myhome.model.Board;
import com.etecus.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@RestController
@RequestMapping("/api")
class BoardApiController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false) String title, @RequestParam(required = false, defaultValue = "") String content) {
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return boardRepository.findAll();
        } else {
            return boardRepository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {

        return boardRepository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

        return boardRepository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    return boardRepository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return boardRepository.save(newBoard);
                });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
}
