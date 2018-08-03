package com.zjlp.httpvolly.test;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;

/**
 * Created by tiny  on 2016/9/14 13:53.
 * Desc: 用于单元测试
 */

public class TestResponseDelivery implements ResponseDelivery {

    public TestResponseDelivery() {
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response) {
        postResponse(request, response, null);
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable) {
        postResult(request, response, runnable);
    }

    @Override
    public void postError(Request<?> request, VolleyError error) {
        request.addMarker("post-error");
        Response<?> response = Response.error(error);
        postResult(request, response, null);
    }

    private void postResult(Request request, Response response, Runnable runnable) {
        final Request mRequest = request;
        final Response mResponse = response;
        if (mRequest.isCanceled()) {
            mRequest.finish("canceled-at-delivery");
            return;
        }

        // Deliver a normal response or error, depending.
        if (mResponse.isSuccess()) {
            mRequest.deliverResponse(mResponse.result);
        } else {
            mRequest.deliverError(mResponse.error);
        }

        // If this is an intermediate response, add a marker, otherwise we're done
        // and the request can be finished.
        if (mResponse.intermediate) {
            mRequest.addMarker("intermediate-response");
        } else {
            mRequest.finish("done");
        }

        // If we have been provided a post-delivery runnable, run it.
        if (runnable != null) {
            runnable.run();
        }
    }
}
