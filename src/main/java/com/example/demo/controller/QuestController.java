package com.example.demo.controller;

import com.example.demo.domain.Plan;
import com.example.demo.domain.Quest;
import com.example.demo.dto.plan.AddPlanRequest;
import com.example.demo.dto.plan.PlanResponse;
import com.example.demo.dto.plan.UpdatePlanRequest;
import com.example.demo.dto.quest.AddQuestRequest;
import com.example.demo.dto.quest.LastThreeQuestsResponse;
import com.example.demo.dto.quest.QuestResponse;
import com.example.demo.dto.quest.UpdateQuestRequest;
import com.example.demo.service.PlanService;
import com.example.demo.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quests")
public class QuestController {
    private final QuestService questService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addQuest(@RequestBody AddQuestRequest request) {

        List<Quest> quests = questService.findAllByUserId(request.getUserId());

        Map<String, Object> response = new HashMap<>();

        if ( quests.size() >= 1 ) {
            response.put("message", "퀘스트가 1개 이상 있어서 더 이상의 추가가 불가합니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Quest savedQuest = questService.save(request);

        if (savedQuest.getQuest() == null || savedQuest.getQuest().trim().isEmpty()) {
            response.put("message", "퀘스트가 빈칸입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);  // BAD REQUEST 상태 코드 반환
        }

        response.put("data", new QuestResponse(savedQuest));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // 이전의 3개 퀘스트 가져오기
    @GetMapping("/{user_id}/previous")
    public List<LastThreeQuestsResponse> getPreviousQuests(@PathVariable("user_id") Long userId) {
        return questService.getPreviousQuests(userId);
    }

    @GetMapping("/{user_id}/{id}")
    public ResponseEntity<QuestResponse> findQuest(@PathVariable("user_id") Long userId,
                                                   @PathVariable("id") Long id) {
        Quest quest = questService.findById(id);

        return ResponseEntity.ok()
                .body(new QuestResponse(quest));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<QuestResponse>> findAllQuests(@PathVariable("user_id") Long userId) {
        List<Quest> quests = questService.findAllByUserId(userId);
        List<QuestResponse> responses = quests.stream()
                .map(QuestResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(responses);
    }


    @PutMapping("/{id}")
    public ResponseEntity<QuestResponse> updateQuest(@PathVariable("id") Long id,
                                                     @RequestBody UpdateQuestRequest request) {
        Quest updateQuest = questService.update(id, request);

        return ResponseEntity.ok()
                .body(new QuestResponse(updateQuest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuest(@PathVariable("id") Long id) {
        questService.delete(id);

        return ResponseEntity.ok()
                .body("id가 "+id+"인 계획이 삭제되었습니다.");
    }
}
