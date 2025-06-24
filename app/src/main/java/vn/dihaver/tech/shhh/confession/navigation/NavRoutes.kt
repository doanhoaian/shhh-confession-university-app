package vn.dihaver.tech.shhh.confession.navigation

object NavRoutes {

    /**
     * AUTH GRAPH: Một nested navigation graph thực sự, quản lý toàn bộ luồng xác thực.
     */
    object Auth {
        const val GRAPH = "auth_graph"
        const val START = "auth_start"
        const val INPUT_EMAIL = "auth_input_email"
        const val INPUT_PASSWORD = "auth_input_password"
        const val INPUT_OTP = "auth_input_otp"
        const val CREATE_PASSWORD = "auth_create_password"
        const val SELECT_ALIAS = "auth_select_alias"
        const val SELECT_SCHOOL = "auth_select_school"
        const val CONFIRM_INFO = "auth_confirm_info"
    }

    object Home {
        const val GRAPH = "home_graph"
        const val FEED = "home_feed"
        // Extend:
        // const val PROFILE = "home_profile"
        // const val NOTIFICATIONS = "home_notifications"
    }

    /**
     * POST GRAPH: Nhóm các màn hình liên quan đến việc tạo và quản lý bài đăng.
     */
    object Post {
        const val GRAPH = "post_graph"
        const val CREATE = "post_create"
        // Extend:
        // const val EDIT = "post_edit/{postId}"
        // const val DETAILS = "post_details/{postId}"
    }
}