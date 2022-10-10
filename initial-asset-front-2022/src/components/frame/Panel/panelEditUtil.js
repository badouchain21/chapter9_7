export default {
    isPage(data) {
        if ((data.pageId == null || data.pageId == "") && (data.pageURL == null || data.pageURL == ""))
            return false;
        else
            return true;
    },
    isFilter(data) {
        if (data.panelFilterReportId == null || data.panelFilterReportId == "")
            return false;
        else
            return true;
    },
    isVideo(data) {//仅支持单一内容的数据是否为视频的判断
        if (data.contents == null || data.contents.length == 0)
            return false;
        else if (data.contents[0].dataSourceType == 4)
            return true;
    },
    isContentSingle(data) {
        if (data.contentType == 0 || data.contentType == null)
            return true;
        else
            return false;
    },
    isContentGroup(data) {
        if (data.contentType == 1)
            return true;
        else
            return false;
    },
    isContentPictures(data) {
        if (data.contentType == 2)
            return true;
        else
            return false;
    }
}