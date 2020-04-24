import handlers from "./handlers";

export default function configureEventSources(source) {
  const eventSource = new EventSource(source);
  handlers.forEach((handler) => {
    console.log("Added new event source for: " + eventSource.url);
    // assign handlers as event listeners to source
    eventSource.addEventListener(handler.eventType, (event) => {
      handler.handle(event); // pass event to handler
    });
  });

  return eventSource;
}
