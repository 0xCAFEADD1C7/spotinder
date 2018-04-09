// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
import * as functions from 'firebase-functions';

// The Firebase Admin SDK to access the Firebase Realtime Database.
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase);

interface TrackListening {
  userId: string;
  date: Date;
  trackId: string;
}

interface Match {
  contactData : string;
  contactType : string;
  matchTitle : string;
  name : string;
  picture : string;
  random : number;
}

interface UserInfo {
  dispName : string;
  email : string;
  phone : string;
  pictureUrl : string;
  uid : string;
  contactType : string;
  sex : string;
  wantedSex : string;
}

interface Track {
  id : string;
  name : string;
  artist : string;
  album : string;
}

export const onTrackListening = functions.firestore.document('/trackListening/{newId}').onCreate(async (event) => {
  const ONE_DAY = 1000 * 3600 * 24 * 15;
  const MAX_MATCH_DAYS = 15;
  const MAX_MATCH_TIME = MAX_MATCH_DAYS * ONE_DAY;

  const data:TrackListening = event.data.data()
  console.log("Data received :", data);

  const {
    trackId,
    date,
    userId
  } = data;

  const newMatchId = event.data.id;  

  const trackCollection = admin.firestore().collection('trackListening');
  const potentialMatch = trackCollection
    .where("trackId", "==", trackId)
    .where("date", ">=", new Date(date.getTime() - MAX_MATCH_TIME));
  ;

  return potentialMatch.get()
  .then(matches => {
        // regarde toutes les ecoutes qui matchent
    matches.docs.reduce((proms : Promise<any>[], match) => {
      console.log("Match:", match.data());
      const mData = match.data() as TrackListening; 
      const matchUserId = mData.userId;

      if (matchUserId !== userId) {
        console.log("Different user id ! Creating match btwn", matchUserId, "and", userId);

        const userInfoCollection = admin.firestore().collection('usersInfos');
        const trackInfoCollection = admin.firestore().collection('trackInfos');
        const matchCollection = admin.firestore().collection('match');

        const matchCreationProm = Promise.all([
          userInfoCollection.doc(matchUserId).get().then(o => o.data() as UserInfo),
          userInfoCollection.doc(userId).get().then(o => o.data() as UserInfo),
          trackInfoCollection.doc(mData.trackId).get().then(o => o.data() as Track)
        ])
        .then(([userIMatch, userIMe, track]) => {
          /**TODO
           * if ( .... ) {
           *  // ici ajouter les conditions de match : sexe, distance, etc
           * }
           */
          const trackTitle = track.artist + " - " + track.name;

          const matchOther : Match = {
            matchTitle : trackTitle,
            contactData : userIMe.contactType === "PHONE" ? userIMe.phone : userIMe.email,
            contactType : userIMe.contactType,
            name : userIMe.dispName,
            picture : userIMe.pictureUrl,
            random : Math.random(),
          }

          const matchMe : Match = {
            matchTitle : trackTitle,
            contactData : userIMatch.contactType === "PHONE" ? userIMatch.phone : userIMatch.email,
            contactType : userIMatch.contactType,
            name : userIMatch.dispName,
            picture : userIMatch.pictureUrl,
            random : Math.random(),
          }

          return [matchOther, matchMe]
        })
        .then(([matchOther, matchMe]) => {
          const addMe = matchCollection
            .doc(userId)
            .collection('matches')
            .add(matchMe)
            .then(() => console.log("ADD!"))

          const addOther = matchCollection
            .doc(matchUserId)
            .collection('matches')
            .add(matchOther)
            .then(() => console.log("ADD!"));

          return Promise.all([addMe, addOther]);
        });

        return proms.concat();
      } else {
        console.log("Same UID, ignoring.");
        return proms;
      }
    }, []);

    if (matches.docs.length === 0) {
      console.log("No match.")
    }
  });
});