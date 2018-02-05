package org.springframework.data.mybatis.repository.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.mybatis.repository.annotation.Query;
import org.springframework.data.mybatis.repository.support.MybatisQueryNotSupportException;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryInformation;

public class QueryAnnotationStatementXmlGenerator {
	private final List<MybatisQueryMethod> queryAnnotationMethods;

	public QueryAnnotationStatementXmlGenerator(RepositoryInformation information) {
		super();
		this.queryAnnotationMethods = parseQueryAnnotationFromReInterface(information);
	}
	
	private List<MybatisQueryMethod> parseQueryAnnotationFromReInterface(RepositoryInformation information) {
    	List<MybatisQueryMethod>  rst = new ArrayList<MybatisQueryMethod>();
		Class<?> repositoryClass = information.getRepositoryInterface();
		ProjectionFactory factory = MybatisQueryNotSupportException.unsupportInstance(ProjectionFactory.class);
		for (Method method : repositoryClass.getMethods()) {
			Query queryAnnoation = method.getAnnotation(Query.class);
			if(queryAnnoation!=null&&queryAnnoation.sql().length()>0) {
				MybatisQueryMethod m = new MybatisQueryMethod(method, information , factory);
				rst.add(m);
			}
		}
		return rst;
	}

	public void render(StringBuilder builder) {
		for (MybatisQueryMethod mybatisQueryMethod : queryAnnotationMethods) {
      	  buildQueryAnnotationStatement(mybatisQueryMethod,builder);
		}
	}

	private void buildQueryAnnotationStatement(MybatisQueryMethod mybatisQueryMethod, StringBuilder builder) {
		Query query = mybatisQueryMethod.getQueryAnnotation();
		String tag;
		switch (query.operation()) {
		case insert:
		case delete:
		case update:
			tag = query.operation().name();
			break;
		case page:
		case select_list:
		case select_one:
		case slice:
		case stream:
			tag = "select";
			break;
		default:
			tag = query.sql().trim().toLowerCase().substring(0, 6);
			break;
		}
		
		if(tag.equals("select")) {
			buildQueryAnnotationSelectStatement(mybatisQueryMethod,builder);
		}else {
			buildQueryAnnotationUpdateStatement(tag,mybatisQueryMethod,builder);
		}
	}

	private void buildQueryAnnotationUpdateStatement(String tag, MybatisQueryMethod mybatisQueryMethod, StringBuilder builder) {
//		MybatisParameters parameters = mybatisQueryMethod.getParameters();
//		String parameterType = parameters.getNumberOfParameters()>1?Map.class.getName():parameters.getParameter(0).getType().getName();
		builder.append("<").append(tag)
		.append(" id=\"").append(mybatisQueryMethod.getName())
//		.append( "\" parameterType=\"").append(parameterType)
		.append("\" lang=\"XML\"><![CDATA[");
        builder.append(mybatisQueryMethod.getQueryAnnotation().sql());
        builder.append("]]></").append(tag).append(">");
	}

	private void buildQueryAnnotationSelectStatement(MybatisQueryMethod mybatisQueryMethod, StringBuilder builder) {
		Class<?> returnedObjectType=mybatisQueryMethod.getReturnedObjectType();
		if(returnedObjectType==null) {
			returnedObjectType = mybatisQueryMethod.getReturnType();
		}
		builder.append("<select")
		.append(" id=\"").append(mybatisQueryMethod.getName())
		.append("\" resultType=\"").append(mybatisQueryMethod.getReturnedObjectType().getName())
		.append( "\" lang=\"XML\"><![CDATA[");
        builder.append(mybatisQueryMethod.getQueryAnnotation().sql());
        builder.append("]]></select>");
	}

}
