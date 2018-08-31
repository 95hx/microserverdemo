package cn.luv2code.sample.userprovider.controller;

import cn.luv2code.sample.userprovider.dto.ArticleDto;
import cn.luv2code.sample.userprovider.kafka.Producer;
import cn.luv2code.sample.userprovider.kafka.SampleMessage;
import cn.luv2code.sample.userprovider.service.ArticleService;
import cn.luv2code.sample.userprovider.utils.Result;
import cn.luv2code.sample.userprovider.utils.ResultStatus;
import cn.luv2code.sample.userprovider.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/article")
public class ArticleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    @Resource
    private ArticleService articleService;
    @Resource
    private Producer producer;
    /**
     * 查
     */
    @GetMapping("/{id}")
    public Result<ArticleDto> findById(@PathVariable Long id) throws Exception {
        producer.send(new SampleMessage(id.intValue(),"msg"));
        return ResultUtils.success(articleService.findById(id));
    }

    /**
     * 查
     */
    @GetMapping("/all")
    public Result findAll() {
        return ResultUtils.success(articleService.findAll());
    }

    /**
     * 查
     */
    @GetMapping("/{page}/{size}")
    public Result findPageAll(@PathVariable Integer page, @PathVariable Integer size) {
        return ResultUtils.success(articleService.findAll(new PageRequest(page - 1, size)));
    }

    /**
     * 增/更新
     */
    @PostMapping("/add")
    public Result add(@Valid @RequestBody ArticleDto articleDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResultUtils.error(ResultStatus.UNKNOWN_ERROR);
        articleService.save(articleDto);
        return ResultUtils.success();
    }

    /**
     * 删
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        articleService.deleteById(id);
        return ResultUtils.success();
    }
}
