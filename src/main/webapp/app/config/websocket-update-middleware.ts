import SockJS from 'sockjs-client';

import Stomp from 'webstomp-client';
import { Observable } from 'rxjs'; // tslint:disable-line
import { Observer } from 'rxjs/Observer'; // tslint:disable-line
import { translate, Storage } from 'react-jhipster';

import { toast } from 'react-toastify';

import { ACTION_TYPES as ACTIONS } from 'app/modules/home/credit-proposal.reducer';
import { ACTION_TYPES as AUTH_ACTIONS } from 'app/shared/reducers/authentication';
import { SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

let stompClient = null;

let subscriber = null;
let connection: Promise<any>;
let connectedPromise: any = null;
let listener: Observable<any>;
let listenerObserver: Observer<any>;
let alreadyConnectedOnce = false;

const createConnection = (): Promise<any> => new Promise((resolve, reject) => (connectedPromise = resolve));

const createListener = (): Observable<any> =>
  new Observable(observer => {
    listenerObserver = observer;
  });

const subscribe = () => {
  connection.then(() => {
    subscriber = stompClient.subscribe('/topic/credit-proposal', data => {
      // tslint:disable-next-line
      console.log('>>>>>>>>>>>>>> 0', data);
      listenerObserver.next(JSON.parse(data.body));
    });
  });
};

const connect = () => {
  if (connectedPromise !== null || alreadyConnectedOnce) {
    // the connection is already being established
    return;
  }
  connection = createConnection();
  listener = createListener();

  // building absolute path so that websocket doesn't fail when deploying with a context path
  const loc = window.location;

  const headers = {};
  let url = '//' + loc.host + loc.pathname + 'websocket/tracker';
  const authToken = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
  if (authToken) {
    url += '?access_token=' + authToken;
  }
  const socket = new SockJS(url);
  stompClient = Stomp.over(socket);

  stompClient.connect(
    headers,
    () => {
      connectedPromise('success');
      connectedPromise = null;
      subscribe();
      if (!alreadyConnectedOnce) {
        alreadyConnectedOnce = true;
      }
    }
  );
};

const disconnect = () => {
  if (stompClient !== null) {
    stompClient.disconnect();
    stompClient = null;
  }
  window.onhashchange = () => {};
  alreadyConnectedOnce = false;
};

const receive = () => listener;

const unsubscribe = () => {
  if (subscriber !== null) {
    subscriber.unsubscribe();
  }
  listener = createListener();
};

export default store => next => action => {
  //  if (action.type === SUCCESS(AUTH_ACTIONS.GET_SESSION)) {
  connect();
  if (!alreadyConnectedOnce) {
    receive().subscribe(activity => {
      // tslint:disable-next-line
      console.log('>>>>>>>>>>>>>> 1', activity);
      toast.success(translate('calcardApp.creditProposal.updated', { param: activity.id }));
      //        toast.success('ié ié');
      return store.dispatch({
        type: ACTIONS.WEBSOCKET_UPDATE_MESSAGE,
        payload: activity
      });
    });
  }
  //  } else if (action.type === FAILURE(AUTH_ACTIONS.GET_SESSION)) {
  //    unsubscribe();
  //    disconnect();
  //  }
  return next(action);
};
