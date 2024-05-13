import { createBrowserRouter } from "react-router-dom";
import { Suspense, lazy } from "react";

const Loading = <div>Loading....</div>
const Home = lazy(() => import("./pages/Home"));
const SignUp = lazy(() => import("./pages/SignUp"));
const Login = lazy(() => import("./pages/Login"));

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
    }
]);

export default router;