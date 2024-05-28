import { createBrowserRouter } from "react-router-dom";
import { Suspense, lazy } from "react";

const Loading = <div>Loading....</div>
const Home = lazy(() => import("./pages/Home"));
const SignUp = lazy(() => import("./pages/SignUp"));
const Login = lazy(() => import("./pages/Login"));
const Gathering = lazy(() => import("./pages/Gathering"));
const NewGathering = lazy(() => import("./pages/NewGathering"));
const LoginResponse = lazy(() => import("./pages/LoginResponse"));

const router = createBrowserRouter([
    {
        path: "",
        element: <Suspense fallback={Loading}><Home /></Suspense>
    },
    {
        path: "/sign-up",
        element: <Suspense fallback={Loading}><SignUp /></Suspense>
    },
    {
        path: "/login",
        element: <Suspense fallback={Loading}><Login /></Suspense>
    },
    {
        path: "/gathering",
        element: <Suspense fallback={Loading}><Gathering /></Suspense>
    },
    {
        path: "/gathering/new",
        element: <Suspense fallback={Loading}><NewGathering /></Suspense>
    },
    {
        path: "/login/response",
        element: <Suspense fallback={Loading}><LoginResponse /></Suspense>
    }
]);

export default router;