document.querySelectorAll('a[href^="#"]').forEach(function(anchor){
    anchor.addEventListener('click', function (e) {
        e.preventDefault();

        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

const template = '<div class="popover" role="tooltip"><div class="arrow"></div><div class="popover-body"></div></div>'
const popoverOptions = {
    html:true,
    placement:"right",
    trigger:"focus",
    template: template
}
const fictionDesc = "<b>Imagery: </b><p>How well the world within the story is described " +
    "and visualized. Does the writing create vivid and realistic ideas in your " +
    "mind as you read? A high score is given to fiction that creates lasting memories.</p>" +
    "<b>Entertainment: </b><p>Was the book enjoyable to read? A good story should flow " +
    "so well that it feels like watching a movie. The plot should keep you engaged.</p>" +
    "<b>Writing: </b><p> The quality of writing in the book. Good style and interesting " +
    "use of language and grammar will score high marks.</p>";
const nonfictionDesc = "<b>Value: </b><p>How much was learned by reading the book and what is the " +
    "importance of what was learned? Was the author biased or misleading? If a book " +
    "has a positive impact on you as a person, it was valuable.</p>" +
    "<b>Interest: </b><p>Reading an entire medical encyclopedia would be very " +
    "valuable...but incredibly boring. Without a strong narrative and an engaging " +
    "style, non-fiction is a chore to read. Good books should keep you " +
    "interested.</p>" +
    "<b>Writing: </b><p> The quality of writing in the book. Good style and interesting " +
    "use of language and grammar will score high marks.</p>";

$('.fiction-score-info').popover({content:fictionDesc, ...popoverOptions})
$('.nonfiction-score-info').popover({content:nonfictionDesc, ...popoverOptions})