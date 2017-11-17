//
//  HotDetailPersonalCell.h
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HotDetailPersonalCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIScrollView *scrollThumb;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblElect;
@property (weak, nonatomic) IBOutlet UILabel *lblReply;
@property (weak, nonatomic) IBOutlet UIView *moreReplyView;
@property (weak, nonatomic) IBOutlet UIButton *moreReplyButton;

- (IBAction)onClickMoreReplyButton:(id)sender;

@end