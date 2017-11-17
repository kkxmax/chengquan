//
//  EvaluateTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvaluateTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIScrollView *scrollThumb;
@property (nonatomic, weak) IBOutlet UILabel *ownerNameLabel;
@property (nonatomic, weak) IBOutlet UITextView *ownerContentTextView;
@property (nonatomic, weak) IBOutlet UILabel *evaluateKindLabel;
@property (nonatomic, weak) IBOutlet UIImageView *avatarImageView;
@property (nonatomic, weak) IBOutlet UILabel *writeTimeLabel;
@property (nonatomic, weak) IBOutlet UILabel *electCountLabel;
@property (nonatomic, weak) IBOutlet UIView *moreReplyView;
@property (nonatomic, weak) IBOutlet UIImageView *isFalseImageView;
@property (nonatomic, weak) IBOutlet UILabel *replyContentLabel;
@property (nonatomic, weak) IBOutlet UIButton *moreReplyButton;
@property (nonatomic, weak) IBOutlet UILabel *moreReplyLabel;

@end
