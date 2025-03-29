package com.dai.mapper;

import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    List<TitleVo> getUserTitles(Long UserId);

    List<HistoryVo> getHistoryDetails(Long memoryId);

    void saveHistory(History history);

    void saveTitle(Title title);

    void deleteHistory(Long memoryId);

    void deleteTitle(Long memoryId);

    void updateTitle(Title newTitle);
}
