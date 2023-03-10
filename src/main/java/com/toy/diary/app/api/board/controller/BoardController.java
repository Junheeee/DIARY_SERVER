package com.toy.diary.app.api.board.controller;

import com.toy.diary.app.api.board.model.BoardDelModel;
import com.toy.diary.app.api.board.model.BoardRsModel;
import com.toy.diary.app.api.board.service.BoardService;
import com.toy.diary.app.api.comn.response.BasicResponse;
import com.toy.diary.app.api.comn.response.ErrorResponse;
import com.toy.diary.app.api.comn.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/board", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BoardController {

    @Autowired
    private BoardService service;

    @GetMapping(value = "/apple")
    public ResponseEntity<? extends BasicResponse> apple() {

        List<BoardRsModel> result = null;
        result = service.apple();

        return ResponseEntity.ok().body(new SuccessResponse<List>(result));
    }

    @PutMapping(value = "/add")
    public ResponseEntity<? extends BasicResponse> add(@RequestBody BoardRsModel rq) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            boolean result = service.add(rq);
            resultMap.put("result", result);
        } catch(Exception e) {
            log.error("IGNORE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }

    @PutMapping(value = "/delete")
    public ResponseEntity<? extends BasicResponse> delete(@RequestBody BoardDelModel rq) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            int result = service.delete(rq);
            resultMap.put("result", result);
        } catch(Exception e) {
            log.error("IGNORE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }

    @GetMapping(value = "/detail")
    public ResponseEntity<? extends BasicResponse> detail(@RequestParam("boardSno") int boardSno) {
        BoardRsModel result = null;
        try{
            result = service.detail(boardSno);
        } catch(Exception e) {
            log.error("IGNORE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(result));
    }

    @PutMapping(value = "/save")
    public ResponseEntity<? extends BasicResponse> save(@RequestBody BoardRsModel rq) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            boolean result = service.save(rq);
            resultMap.put("result", result);
        } catch(Exception e) {
            log.error("IGNORE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }
}
