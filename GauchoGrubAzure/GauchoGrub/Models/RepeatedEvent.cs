﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * RepeatedEvent - model for representing recurring events.
     * RepeatedEvent is related to a specific Dining Common and Meal.
     * It is assumed to repeat every week on a specific day and during specified time.
     */
    [DataContract]
    public class RepeatedEvent
    {
        public int Id { get; set; }

        [Required]
        public int DiningCommonId { get; set; }  // Foreign Key

        [DataMember]
        [ForeignKey("DiningCommonId")]
        public DiningCommon DiningCommon { get; set; }  // Navigation Property

        [Required]
        public int MealId { get; set; }  // Foreign Key

        [DataMember]
        [ForeignKey("MealId")]
        public Meal Meal { get; set; }  // Navigation property

        [Required]
        [DataMember]
        public TimeSpan From { get; set; }

        [Required]
        [DataMember]
        public TimeSpan To { get; set; }

        [Required]
        [DataMember]
        public DayOfWeek DayOfWeek { get; set; }
    }
}