package cn.darren.service;

import cn.darren.entity.Evaluation;

import java.util.List;

public interface EvaluationService {
    public List<Evaluation> selectByBookId(Long bookId);
}
