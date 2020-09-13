package rpc;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.Item;

/**
 * Servlet implementation class ItemsHistory
 */
public class ItemsHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ItemsHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		String userId = request.getParameter("user_id");
		
		MySQLConnection connection = new MySQLConnection();
		Set<Item> items = connection.getFavoriteItems(userId);
		connection.close();
		
		JSONArray array = new JSONArray();
		for (Item item : items) {
			JSONObject obj = item.toJSONObject();
			obj.put("favorite", true); // 为了让前端实心显示favorite
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		MySQLConnection connection = new MySQLConnection();
		JSONObject input = new JSONObject(IOUtils.toString(request.getReader())); // 读取request的body并转化成JSONObject
		String userId = input.getString("user_id");
		Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));
		
		connection.setFavoriteItems(userId, item);
		connection.close();
		RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		MySQLConnection connection = new MySQLConnection();
		JSONObject input = new JSONObject(IOUtils.toString(request.getReader()));
		String userId = input.getString("user_id");
		Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

		connection.unsetFavoriteItems(userId, item.getItemId());
		connection.close();
		RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	}
	
}
