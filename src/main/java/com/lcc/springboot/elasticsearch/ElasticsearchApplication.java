package com.lcc.springboot.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 李歘歘
 */
@SpringBootApplication
@RestController
public class ElasticsearchApplication {

	/***
	 * 自动注入
	 */
	@Autowired
	private TransportClient client;

	/**
	 * 根据Id查询书籍
	 * @param id
	 * @return
	 */
	@GetMapping("/get/book/novel")
	@ResponseBody
	public ResponseEntity get(@RequestParam(name = "id",defaultValue = "") String id){
		//当传进来的id为空时返回404
		if (id.isEmpty()){
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		//获得传进来的值以及索引和类型等
		GetResponse result = this.client.prepareGet("book","novel",id).get();
		//当传进来的id不存在时返回404
		if (!result.isExists()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(result.getSource(), HttpStatus.OK);
	}


	/**
	 * 添加书籍
	 * @param title
	 * @param author
	 * @param wordCount
	 * @param publishDate
	 * @return
	 */
	@PostMapping("/add/book/novel")
	@ResponseBody
	public ResponseEntity add(
			@RequestParam(name = "title") String title,
			@RequestParam(name = "author") String author,
			@RequestParam(name = "word_count") int wordCount,
			@RequestParam(name = "publish_date")
					//限定日期格式
					@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
			Date publishDate ) {
		try {
			//构建json
			XContentBuilder content = XContentFactory.jsonBuilder()
					.startObject()
					.field("title",title)
					.field("author",author)
					.field("word_count",wordCount)
					.field("publish_date",publishDate.getTime())
					.endObject();
			//将json传进去并构建
			IndexResponse result = this.client.prepareIndex("book","novel")
					.setSource(content)
					.get();
			return new ResponseEntity(result.getId(),HttpStatus.OK);
		}catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * 删除书籍
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/book/novel")
	@ResponseBody
	public ResponseEntity delete(@RequestParam(name = "id") String id) {
		DeleteResponse result = this.client.prepareDelete("book","novel",id).get();
		return new ResponseEntity(result.getResult().toString(),HttpStatus.OK);
	}


	/**
	 * 更新书籍
	 * @param id
	 * @param title
	 * @param author
	 * @return
	 */
	@PutMapping("/update/book/novel")
	@ResponseBody
	public ResponseEntity update(
			@RequestParam(name = "id") String id,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "author", required = false) String author ) {
		UpdateRequest update = new UpdateRequest("book","novel",id);
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject();
			//如果title和author不为空则传进来
			if (title != null) {
				builder.field("title",title);
			}
			if (author != null) {
				builder.field("author",author);
			}
			//builder一定要以endObject()结尾
			builder.endObject();
			update.doc(builder);
		}catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			//更新文档
			UpdateResponse result = this.client.update(update).get();
			return new ResponseEntity(result.getResult().toString(),HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * 复合查询书籍---检索
	 * @param author
	 * @param title
	 * @param gtWordCount
	 * @param ltWordCount
	 * @return
	 */
	@PostMapping("/query/book/novel")
	@ResponseBody
	public ResponseEntity query (
			@RequestParam(name = "author", required = false) String author,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "gt_word_count", defaultValue = "0") int gtWordCount,
			@RequestParam(name = "lt_word_count", required = false ) Integer ltWordCount ){

		//构建布尔查询
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		if (author != null){
			boolQuery.must(QueryBuilders.matchQuery("author",author));
		}
		if (title != null){
			boolQuery.must(QueryBuilders.matchQuery("title",title));
		}

		//构建范围查询
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word_count")
										.from(gtWordCount);
		if (ltWordCount != null && ltWordCount > 0 ) {
			rangeQuery.to(ltWordCount);
		}

		//将rangeQuery和boolQuery用filter结合起来
		boolQuery.filter(rangeQuery);

		SearchRequestBuilder builder = this.client.prepareSearch("book")
				.setTypes("novel")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery)
				.setFrom(0)
				.setSize(10);

		System.out.println(builder);
		//获取返回结果，返回结果是一个hits
		SearchResponse response = builder.get();
		//创建存放返回结果的list
		List<Map<String, Object>> result = new ArrayList<>();

		//遍历取出的hits，存入result
		for (SearchHit hit : response.getHits()) {
			result.add(hit.getSource());
		}
		return new ResponseEntity(result, HttpStatus.OK);
	}

	/**
	 * 启动方法
	 * @param args
	 */

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchApplication.class, args);
	}

}
