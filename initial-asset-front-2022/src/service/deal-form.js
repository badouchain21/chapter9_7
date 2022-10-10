// 存放表单处理的相关函数

/**
 * 将表单列表数据按照一列24占比进行重新划分，修改数据结构，这样一行的元素不会影响下一行的展示，避免交错
 * @params showList:[Array] 需要返回的展示列表
 * @params dataList:[Array] 初始数据列表
 * @params columnName:[String] 字段名称
 * <el-row>                                  <el-row>
 *      <el-col :span="6"></el-col>               <el-col :span="6"></el-col>
 *      <el-col :span="18"></el-col>  --->        <el-col :span="18"></el-col>
 *      <el-col :span="6"></el-col>           </el-row>
 *      <el-col :span="18"></el-col>          <el-row>
 *  </el-row>                                     <el-col :span="6"></el-col>
 *                                               <el-col :span="18"></el-col>
 *                                           </el-row>
 */
export function makeFormShowList (showList, dataList, columnName) {
    // 定义临时存储列表数据
    let tempList = []
    // 定义当前列占比数
    let columnNum = 0
    // 定义最高列占比，参照elementUI，一行占比数峰值是24比例列
    let MaxcolumnNum = 24
    let formShowList = dataList
    formShowList.forEach((i, index) => {
        let itemColumnNum = i[columnName]
        if (columnNum + itemColumnNum <= MaxcolumnNum) {
            columnNum += itemColumnNum
            tempList.push(i)
        } else {
            showList.push(tempList)
            tempList = []
            tempList.push(i)
            columnNum = itemColumnNum
        }
        if (index === formShowList.length - 1) {
            showList.push(tempList)
        }
    })
}