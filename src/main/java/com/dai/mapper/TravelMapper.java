package com.dai.mapper;

import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelMapper {

    void saveHistory(History user);

    void updateTitle(Title newTitle);

    List<TitleVo> getUserTitles(Long id);

    List<HistoryVo> getHistoryDetails(Long id);

    void saveTitle(Title title);

    void deleteHistory(Long memoryId);

    void deleteTitle(Long memoryId);
}
