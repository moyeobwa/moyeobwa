import { createBrowserRouter } from "react-router-dom";
import { Suspense, lazy } from "react";

const Loading = <div>Lading....</div>
const Home = lazy(() => import("./pages/Home"));
const SignUp = lazy(() => import("./pages/SignUp"));

const router = createBrowserRouter([
    {
        path: "",
        element: <Suspense fallback={Loading}><Home /></Suspense>
    },
    {
        path: "/sign-up",
        element: <Suspense fallback={Loading}><SignUp /></Suspense>
    }
]);

export default router;