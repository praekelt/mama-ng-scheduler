class UrlMappings {

	static mappings = {
        "/rest/schedules" {controller = "schedule"; action = [POST: 'create', GET: 'list']}
        "/rest/schedules/$id" {controller = "schedule"; action = [PUT: 'update', GET: 'show', DELETE: 'delete']}
        "/rest/schedules/$id/messages" {controller = "schedule"; action = [GET: 'messages']}
        "/rest/messages" {controller = "message"; action = [POST: 'create', GET: 'list']}
        "/rest/messages/$id" {controller = "message"; action = [PUT: 'update', GET: 'show', DELETE: 'delete']}
        "/rest/users" {controller = "user"; action = [POST: 'create', GET: 'list']}
        "/rest/users/$id" {controller = "user"; action = [PUT: 'update', GET: 'show', DELETE: 'delete']}
	}
}
