package com.dai.mapper;

import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MedicalMapper {

    void saveHistory(History history);

    void saveTitle(Title title);

    List<TitleVo> getUserTitles(Long id);

    void updateTitle(Title build);

    void deleteHistory(Long memoryId);

    void deleteTitle(Long memoryId);

    List<HistoryVo> getHistoryDetails(Long id);
}
