package com.project001.util;

import com.project001.vo.ResultVO;



//为了在开发中，返回到前端的数据内容格式趋于一致，我们在开发过程中最好能够将返回数据对象的格式进行约定，以便于开发对接过程中的约定速成
//定义了统一的的返回对象，那么一般我们需要考虑不同场景的输出和调用；比如成功，失败，或其他异常等情况的便捷调用。如果是因为某种业务原因需要返回失败操作，一般包含有错误码和错误信息，更有胜者包含对应的错误堆栈异常明细；那么就需要我们对于错误码做比较好的规划和设计了；
public class ResultVOUtil {
    //成功返回数据
    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();

        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(data);
        return resultVO;
    }

    //失败传入失败信息
    public static ResultVO fail(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        resultVO.setMsg(msg);
        return resultVO;
    }
}